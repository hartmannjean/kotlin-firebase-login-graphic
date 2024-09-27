# Kotlin Grafico App com Login

Este projeto é um aplicativo Kotlin que demonstra como implementar autenticação com Firebase e visualizar gráficos em uma aplicação Android. O projeto utiliza Jetpack Compose para a interface do usuário e inclui exemplos de gráficos usando a biblioteca MPAndroidChart.

![image](https://github.com/user-attachments/assets/4d701766-c737-43b5-903d-3cad84f03ba4)


## Funcionalidades

- **Autenticação com Firebase:** Permite aos usuários se autenticarem usando email e senha.
- **Gráficos Interativos:** Exibe gráficos com dados mockados usando MPAndroidChart
- **Navegação Simples:** Utiliza Jetpack Compose para navegação entre telas de login e home.

## Tecnologias

- **Kotlin:** Linguagem de programação principal para o desenvolvimento Android.
- **Firebase Authentication:** Serviço para autenticação de usuários com email e senha(https://firebase.google.com/docs/auth?hl=pt).
- **Jetpack Compose:** Framework moderno para construção de interfaces de usuário em Android.
- **MPAndroidChart:** Biblioteca para visualização de gráficos.

### Dependências

O projeto utiliza as seguintes bibliotecas:

- Firebase Authentication
- MPAndroidChart
- Jetpack Compose

Adicione as dependências no arquivo `build.gradle` do módulo:

```gradle
dependencies {
    implementation("com.google.firebase:firebase-auth-ktx:21.1.0")
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("androidx.compose.ui:ui:1.4.0")
    implementation("androidx.compose.material:material:1.4.0")
    implementation("androidx.navigation:navigation-compose:2.6.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.0")
}
